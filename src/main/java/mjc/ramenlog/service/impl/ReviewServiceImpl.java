// src/main/java/mjc/ramenlog/service/impl/ReviewServiceImpl.java
package mjc.ramenlog.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.*;
import mjc.ramenlog.dto.ReviewRequestDto;
import mjc.ramenlog.dto.ReviewResponseDto;
import mjc.ramenlog.exception.NotFoundMemberException;
import mjc.ramenlog.exception.NotFoundRestaurantException;
import mjc.ramenlog.exception.NotFoundReviewException;
import mjc.ramenlog.repository.MemberRepository;
import mjc.ramenlog.repository.RestaurantRepository;
import mjc.ramenlog.repository.ReviewImageRepository;
import mjc.ramenlog.repository.ReviewRepository;
import mjc.ramenlog.service.inf.ReviewFileStorageService;
import mjc.ramenlog.service.inf.ReviewService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

    @Value("${cloudinary.name}")
    private String cloudinaryName;
    @Value("${cloudinary.key}")
    private String cloudinaryKey;
    @Value("${cloudinary.secret}")
    private String cloudinarySecret;

    @Override
    public void saveReview(Long memberId, ReviewRequestDto dto, List<MultipartFile> images) {
        // 1) memberId 로 Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        // 2) dto.getRestaurantId() 로 Restaurant 조회
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(NotFoundRestaurantException::new);

        // 3) Review 엔티티 생성 & 저장
        Review review = Review.builder()
                .rating(dto.getRating())
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        review.setRestaurantAndMember(restaurant, member);
        reviewRepository.save(review); // Review를 먼저 저장하여 ID를 생성합니다.

        // 4) 이미지가 있으면 Cloudinary에 업로드하고 저장
        if (images != null && !images.isEmpty()) {
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudinaryName,
                    "api_key", cloudinaryKey,
                    "api_secret", cloudinarySecret
            ));

            images.forEach(file -> {
                if (!file.isEmpty()) {
                    try {
                        String publicId = "review_images/" + UUID.randomUUID().toString();
                        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                                "public_id", publicId
                        ));
                        String url = (String) uploadResult.get("secure_url");

                        ReviewImage ri = new ReviewImage(review, url);
                        reviewImageRepository.save(ri);

                    } catch (IOException e) {
                        throw new RuntimeException("리뷰 이미지 저장 실패 (Cloudinary)", e);
                    }
                }
            });
        }

        // 5) restaurant.review 리스트를 이용해 리뷰 수와 평균 계산
        int reviewCount = restaurant.getReview().size();
        double totalRating = restaurant.getReview().stream()
                .mapToDouble(Review::getRating)
                .sum();
        double averageRating = totalRating / reviewCount;

        // 공식에 따라 score 계산
        double computedScore = averageRating * 2.0
                + Math.log(reviewCount + 1.0) * 3.0;

        // 소수점 첫째 자리까지 반올림
        computedScore = Math.round(computedScore * 10.0) / 10.0;
        averageRating = Math.round(averageRating * 10.0) / 10.0;

        // 6) restaurant.score와 avgRating 업데이트
        restaurant.setScore(computedScore);
        restaurant.setAvgRating(averageRating);

        // 등급 계산
        member.setGrade(Grade.fromReviewCount(member.getReview().size()));
    }

    @Override
    public List<ReviewResponseDto> listReviewByMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<Review> reviews = reviewRepository.findByMember(member)
                .orElseThrow(NotFoundReviewException::new);

        return reviews.stream()
                .map(ReviewResponseDto::new)
                .toList();
    }

    @Override
    public List<ReviewResponseDto> listReviewByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundRestaurantException::new);

        List<Review> reviews = reviewRepository.findByRestaurant(restaurant)
                .orElseThrow(NotFoundReviewException::new);

        return reviews.stream()
                .map(ReviewResponseDto::new)
                .toList();
    }
}

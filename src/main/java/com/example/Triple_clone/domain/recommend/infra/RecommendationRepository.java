package com.example.Triple_clone.domain.recommend.infra;

import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.domain.recommend.domain.RecommendationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    @Query("select r from Recommendation r where (:type is null or r.type = :type) order by function('RAND')")
    List<Recommendation> findRandomByType(@Param("type") String typeNameOrNull,
                                          @Param("limit") int limit);

    @Query("""
           select distinct r
           from Recommendation r
           left join r.tags t
           where (:type is null or r.type = :type)
             and (
                   :keyword is null or :keyword = '' or
                   r.title like concat('%', :keyword, '%') or
                   r.subTitle like concat('%', :keyword, '%') or
                   t like concat('%', :keyword, '%')
                 )
           """)
    List<Recommendation> searchByKeywordAndType(@Param("keyword") String keyword,
                                                @Param("type") RecommendationType type);

    List<Recommendation> findTop10ByTypeOrderByLikesCountDesc(RecommendationType type);

    @Modifying
    @Query("UPDATE Recommendation r SET r.likesCount = r.likesCount + :delta WHERE r.id = :recId")
    void incrementLikeCount(@Param("recId") Long recId, @Param("delta") int delta);
}

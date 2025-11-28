package com.sku.software.moacc.domain.product.entity;

import com.sku.software.moacc.global.common.BaseTimeEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "category_option_types",
        uniqueConstraints = @UniqueConstraint(name = "uk_category_code", columnNames = {"category_id", "code"})
)
public class CategoryOptionType extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    private Boolean active = true;

    // 업데이트용 메서드
    public void update(String code, String name, Boolean active) {
        if (code != null) this.code = code;
        if (name != null) this.name = name;
        if (active != null) this.active = active;
    }
}
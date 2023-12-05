package com.kantboot.functional.file.domain.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 文件组路径记录
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "functional_file_group_path_record")
@DynamicUpdate
@DynamicInsert
public class FunctionalFileGroupPathRecord {

    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 文件组修改的次数
     */
    @Column(name = "number_Of_times")
    private Integer numberOfTimes;

    /**
     * 文件组编码
     */
    @Column(name = "group_code",length = 64)
    private String groupCode;

    /**
     * 文件组路径
     */
    @Column(name = "path")
    private String path;

    /**
     * 文件组路径名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 文件组路径描述
     */
    @Column(name = "description")
    private String description;

}

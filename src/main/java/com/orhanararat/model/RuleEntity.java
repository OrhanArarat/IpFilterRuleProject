package com.orhanararat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE RULE set is_deleted = true, deleted_at = current_timestamp where id = ?")
@Entity
@Table(name = "RULE")
public class RuleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_seq_gen")
    @SequenceGenerator(name = "rule_seq_gen", sequenceName = "SEQ_RULE", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Embedded
    @AttributeOverride(name = "startIp", column = @Column(name = "source_start_ip"))
    @AttributeOverride(name = "endIp", column = @Column(name = "source_end_ip"))
    private IPAddressRange sourceRange;

    @Embedded
    @AttributeOverride(name = "startIp", column = @Column(name = "destination_start_ip"))
    @AttributeOverride(name = "endIp", column = @Column(name = "destination_end_ip"))
    private IPAddressRange destinationRange;

    private Long priority;

    private Boolean allow;

}

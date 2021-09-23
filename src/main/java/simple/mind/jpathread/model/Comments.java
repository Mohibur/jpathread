package simple.mind.jpathread.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GenerationType;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "comments")
@ToString
public class Comments {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "post_id")
	private Integer postId;
	private Integer score;
	@Column(columnDefinition = "TEXT")
	private String text;
	@Column(name = "creation_date")
	private Date creationDate;
	@Column(name = "user_display_name")
	private String userDisplayName;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "content_license")
	private String contentLicense;
}

/*
@formatter:off
-------------------------------
CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `text` text DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `user_display_name` varchar(100) DEFAULT NULL,
  `user_id` mediumtext DEFAULT NULL,
  `content_license` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4


@formatter:on
*/
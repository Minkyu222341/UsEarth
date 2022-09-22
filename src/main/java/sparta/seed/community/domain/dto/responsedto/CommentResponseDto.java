package sparta.seed.community.domain.dto.responsedto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
	private Long commentId;
	private LocalDateTime creatAt;
	private String nickname;
	private String content;
	private String img;
	private boolean writer;


	@Builder
	public CommentResponseDto(Long commentId, LocalDateTime creatAt, String nickname, String content, String img, Boolean writer) {
		this.commentId = commentId;
		this.creatAt = creatAt;
		this.nickname = nickname;
		this.content = content;
		this.img = img;
		this.writer = writer;
	}
}

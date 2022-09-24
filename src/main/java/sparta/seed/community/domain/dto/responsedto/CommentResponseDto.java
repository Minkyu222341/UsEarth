package sparta.seed.community.domain.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
	private Long commentId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
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

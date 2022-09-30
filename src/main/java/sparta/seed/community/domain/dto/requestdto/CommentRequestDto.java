package sparta.seed.community.domain.dto.requestdto;

import lombok.Getter;
import okhttp3.internal.http2.ErrorCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class CommentRequestDto {
	@NotEmpty(message = "내용을 입력해주세요.")
	private String content;
	private boolean delete;
	private String changeNickname;
}

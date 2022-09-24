package sparta.seed.community.domain.dto.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CommentResponseListDto {
	private Long proofId;
	private String dateStatus;
	private List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

	public CommentResponseListDto(Long proofId, String dateStatus) {
		this.proofId = proofId;
		this.dateStatus = dateStatus;
	}
	public void addCommentResponseDto(CommentResponseDto commentResponseDto){
		this.commentResponseDtoList.add(commentResponseDto);
	}
}

package sparta.seed.community.domain.dto.responsedto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProofCountResponseDto {
	private Long proofId;
	private int commentCnt;
	private int heartCnt;
	private boolean participant;
	private boolean heart;

	@Builder
	public ProofCountResponseDto(Long proofId, int commentCnt, int heartCnt, boolean participant, boolean heart) {
		this.proofId = proofId;
		this.commentCnt = commentCnt;
		this.heartCnt = heartCnt;
		this.participant = participant;
		this.heart = heart;
	}
}

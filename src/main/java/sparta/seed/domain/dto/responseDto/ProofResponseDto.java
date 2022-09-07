package sparta.seed.domain.dto.responseDto;

import lombok.Builder;
import lombok.Getter;
import sparta.seed.domain.Img;

import java.util.List;

@Getter
public class ProofResponseDto {

	private Long proofId;
	private String title;
	private String content;
	private List<Img> img;
	private int commentCnt;
	private int heartCnt;
	private boolean writer;
	private boolean heart;

	@Builder
	public ProofResponseDto(Long proofId, String title, String content, List<Img> img, int commentCnt, int heartCnt, boolean writer, boolean heart) {
		this.proofId = proofId;
		this.title = title;
		this.content = content;
		this.img = img;
		this.commentCnt = commentCnt;
		this.heartCnt = heartCnt;
		this.writer = writer;
		this.heart = heart;
	}
}

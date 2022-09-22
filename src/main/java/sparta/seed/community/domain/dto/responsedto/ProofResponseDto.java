package sparta.seed.community.domain.dto.responsedto;

import lombok.Builder;
import lombok.Getter;
import sparta.seed.img.domain.Img;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProofResponseDto {

	private Long proofId;
	private LocalDateTime creatAt;
	private String nickname;
	private String profileImage;
	private String title;
	private String content;
	private List<Img> img;
	private int commentCnt;
	private int heartCnt;
	private boolean writer;
	private boolean heart;

	@Builder
	public ProofResponseDto(Long proofId, LocalDateTime creatAt, String nickname, String profileImage,String title, String content, List<Img> img, int commentCnt, int heartCnt, boolean writer, boolean heart) {
		this.proofId = proofId;
		this.creatAt = creatAt;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.title = title;
		this.content = content;
		this.img = img;
		this.commentCnt = commentCnt;
		this.heartCnt = heartCnt;
		this.writer = writer;
		this.heart = heart;
	}
}

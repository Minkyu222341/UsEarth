package sparta.seed.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.seed.community.domain.Slang;
import sparta.seed.community.repository.SlangRepository;
import sparta.seed.exception.CustomException;
import sparta.seed.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SlangService {
	private final SlangRepository slangRepository;

	public void checkSlang(String word){

		List<Slang> slangList = slangRepository.findAll();

		for (Slang slang : slangList) {
			if(word.contains(slang.getContent())){
				throw new CustomException(ErrorCode.DISCOVER_SLANG);
			}
		}
	}
}

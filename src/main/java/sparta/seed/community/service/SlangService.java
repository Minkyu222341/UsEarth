package sparta.seed.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.seed.community.domain.Slang;
import sparta.seed.community.repository.SlangRepository;
import sparta.seed.exception.CustomException;
import sparta.seed.exception.ErrorCode;
import sparta.seed.util.RedisService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SlangService {
	private final SlangRepository slangRepository;
	private final RedisService redisService;

	public void addSlang(){
		List<Slang> slangList = slangRepository.findAll();
		for(Slang slang : slangList){
			redisService.addSlang(slang.getContent());
		}
	}

	public void checkSlang(String word){
		Set<String> slangList = redisService.getSlangSet();
		for (String slang : slangList) {
			if(word.contains(slang)){
				throw new CustomException(ErrorCode.DISCOVER_SLANG);
			}
		}
	}
}

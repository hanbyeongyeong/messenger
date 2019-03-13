package com.kh.messenger.client2;

import java.util.HashMap;
import java.util.Map;

import com.kh.messenger.common.MemberDTO;

public class Member {

	static Map<String, MemberDTO> mem = new HashMap<>();
	
	static {
		mem = new HashMap<>();
		MemberDTO memberDTO =new MemberDTO();
		memberDTO.setId("ad");
		memberDTO.setPw("ad");
		memberDTO.setNickname("°ü¸®ÀÚ");
		memberDTO.setTel("1");
		memberDTO.setBirth("2018-11-01");
		mem.put("ad", memberDTO);
	}
			
	
	
	
	private Member() {
		
	}
	
		public static Map<String,MemberDTO> getInstance() {
		return mem;
	}
}
	


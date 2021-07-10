package com.mycomp.notesApp.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * This is Util class for Note mapping it uses ModelMapper
 * 
 * @author Rahil
 *
 */
@Component
@Data
public class DTOMapper  implements InitializingBean{
	private ModelMapper mapper;
	public DTOMapper() {
		mapper = new ModelMapper();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//TypeMap<Note, NotesSummaryTO> typeMap = getMapper().createTypeMap(Note.class, NotesSummaryTO.class);
		//typeMap.addMappings(mapper -> mapper.skip(NotesSummaryTO::setNoteID));
	}
}

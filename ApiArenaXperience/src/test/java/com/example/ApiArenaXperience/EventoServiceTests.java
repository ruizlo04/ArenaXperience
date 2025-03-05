package com.example.ApiArenaXperience;


import com.example.ApiArenaXperience.dto.event.CreateEventRequest;
import com.example.ApiArenaXperience.dto.event.EditEventoCmd;
import com.example.ApiArenaXperience.dto.event.EventoResponse;
import com.example.ApiArenaXperience.error.event.EventNotFoundException;
import com.example.ApiArenaXperience.files.model.FileMetadata;
import com.example.ApiArenaXperience.files.service.StorageService;
import com.example.ApiArenaXperience.model.event.Evento;
import com.example.ApiArenaXperience.repo.EventRepository;
import com.example.ApiArenaXperience.repo.TicketRepository;
import com.example.ApiArenaXperience.repo.UserRepository;
import com.example.ApiArenaXperience.service.event.EventoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EventoServiceTests {


	@Mock
	private EventRepository eventRepository;


	@Mock
	private TicketRepository ticketRepository;


	@Mock
	private UserRepository userRepository;


	@Mock
	private StorageService storageService;


	@InjectMocks
	private EventoService eventoService;


	private Evento evento;


	@BeforeEach
	void setUp() {
		evento = Evento.builder()
				.name("Test Event")
				.date(LocalDate.now())
				.capacity(100)
				.price(50.0)
				.file("test.jpg")
				.build();
	}


	@Test
	void getAllEvents_ShouldReturnPageOfEvents() {
		Pageable pageable = mock(Pageable.class);
		Page<Evento> eventPage = new PageImpl<>(List.of(evento));
		when(eventRepository.findAll(pageable)).thenReturn(eventPage);


		Page<EventoResponse> result = eventoService.getAllEvents(pageable);


		assertFalse(result.isEmpty());
		verify(eventRepository, times(1)).findAll(pageable);
	}


	@Test
	void getAllEvents_ShouldThrowEventNotFoundException_WhenNoEventsFound() {
		Pageable pageable = mock(Pageable.class);
		when(eventRepository.findAll(pageable)).thenReturn(Page.empty());


		assertThrows(EventNotFoundException.class, () -> eventoService.getAllEvents(pageable));
	}


	@Test
	void createEvent_ShouldSaveAndReturnEvent() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));


		CreateEventRequest requestDto = new CreateEventRequest("Test Event", LocalDate.now(), 100, 50.0);
		MultipartFile file = mock(MultipartFile.class);


		FileMetadata fileMetadata = mock(FileMetadata.class);
		when(fileMetadata.getFilename()).thenReturn("test.jpg");


		when(storageService.store(file)).thenReturn(fileMetadata);


		Evento evento = new Evento();
		evento.setName("Test Event");
		when(eventRepository.save(any(Evento.class))).thenReturn(evento);


		Evento savedEvent = eventoService.createEvent(requestDto, file);


		assertNotNull(savedEvent);
		assertEquals("Test Event", savedEvent.getName());
		verify(eventRepository, times(1)).save(any(Evento.class));
	}


	@Test
	void editarEvento_ShouldUpdateAndReturnEvent() {
		EditEventoCmd editEventoCmd = new EditEventoCmd(LocalDate.now().plusDays(5), 200);
		when(eventRepository.findByName("Test Event")).thenReturn(Optional.of(evento));
		when(eventRepository.save(any(Evento.class))).thenReturn(evento);


		Evento updatedEvent = eventoService.editarEvento("Test Event", editEventoCmd);


		assertNotNull(updatedEvent);
		assertEquals(200, updatedEvent.getCapacity());
		verify(eventRepository, times(1)).save(any(Evento.class));
	}


	@Test
	void editarEvento_ShouldThrowEventNotFoundException_WhenEventNotFound() {
		EditEventoCmd editEventoCmd = new EditEventoCmd(LocalDate.now().plusDays(5), 200);
		when(eventRepository.findByName("Nonexistent Event")).thenReturn(Optional.empty());


		assertThrows(EventNotFoundException.class, () -> eventoService.editarEvento("Nonexistent Event", editEventoCmd));
	}


}

package G3.jio.services;

import G3.jio.DTO.EventDTO;
import G3.jio.DTO.QueryDTO;
import G3.jio.entities.Event;
import G3.jio.entities.EventRegistration;
import G3.jio.entities.Organiser;
import G3.jio.entities.Status;
import G3.jio.entities.Student;
import G3.jio.exceptions.EventNotFoundException;
import G3.jio.exceptions.InvalidUserTypeException;
import G3.jio.exceptions.UserNotFoundException;
import G3.jio.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private List<Student> studentList;

    private List<EventRegistration> eventRegistrationList;

    private Event event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Student student1 = new Student();
        student1 = new Student();
        student1.setName("Daniel1");
        student1.setEmail("test1@test.com");

        Student student2 = new Student();
        student2 = new Student();
        student2.setName("Daniel2");
        student2.setEmail("test2@test.com");

        Student student3 = new Student();
        student3 = new Student();
        student3.setName("Daniel3");
        student3.setEmail("test3@test.com");

        studentList = List.of(student1, student2, student3);

        event = new Event();
        event.setId(1L);
        event.setName("event 1");
        event.setDescription("random description 1");
        // Event event2 = new Event();
        // event2.setId(2L);
        // event2.setName("event 2");
        // event2.setDescription("random description 2");
        // Event event3 = new Event();
        // event3.setId(3L);
        // event3.setName("event 3");
        // event3.setDescription("random description 3");
        // List<Event> eventList = List.of(event1, event2, event3);

        EventRegistration eventRegistration1 = new EventRegistration();
        eventRegistration1.setEvent(event);
        eventRegistration1.setStudent(student1);
        eventRegistration1.setStatus(Status.PENDING);

        EventRegistration eventRegistration2 = new EventRegistration();
        eventRegistration2.setEvent(event);
        eventRegistration2.setStudent(student2);
        eventRegistration2.setStatus(Status.ACCEPTED);

        EventRegistration eventRegistration3 = new EventRegistration();
        eventRegistration3.setEvent(event);
        eventRegistration3.setStudent(student3);
        eventRegistration3.setStatus(Status.REJECTED);

        eventRegistrationList = List.of(eventRegistration1, eventRegistration2,
                eventRegistration3);

        event.setRegistrations(eventRegistrationList);
    }

    @Test
    void findAllEvent_FindAll_Success() {
        // Arrange
        Event event1 = new Event();
        event1.setName("event1");
        Event event2 = new Event();
        event2.setName("event2");
        Event event3 = new Event();
        event3.setName("event3");
        List<Event> events = List.of(event1, event2, event3);
        when(eventRepository.findAll()).thenReturn(events);

        // Act
        List<Event> result = eventService.findAllEvent();

        // Assert
        assertEquals(events, result);
    }

    @Test
    void getEventById_Exists_Success() {
        // Arrange
        Long eventId = 1L;
        Event event = new Event();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // Act
        Event result = eventService.getEvent(eventId);

        // Assert
        assertEquals(event, result);
    }

    @Test
    void getEventById_NotFound_Failure_ThrowEventNotFound() {
        String exceptionMsg = "";
        // Arrange
        Event event = new Event();
        event.setId(1L);
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act
        try {
            eventService.getEvent(event.getId());
        } catch (EventNotFoundException e) {

            exceptionMsg = e.getMessage();

        }

        // Assert
        assertEquals("Event Not Found: Event does not exist!", exceptionMsg);
    }

    @Test
    void getEventByName_Exist_Success() {
        // Arrange
        Event event1 = new Event();
        event1.setName("common_event_name");
        Event event2 = new Event();
        event2.setName("common_event_name");

        List<Event> events = List.of(event1, event2);
        when(eventRepository.findAllByName(any(String.class))).thenReturn(events);

        // Act
        List<Event> result = eventService.getEventByName("common_event_name");

        // Assert
        assertEquals(events, result);
    }

    @Test
    void getEventByName_NotFound_Failure_ThrowsEventNotFound() {
        String exceptionMsg = "";
        // Arrange
        Event event = new Event();
        event.setName("event");
        List<Event> events = new ArrayList<>();
        when(eventRepository.findAllByName(any(String.class))).thenReturn(events);

        // Act
        try {
            eventService.getEventByName(event.getName());
        } catch (EventNotFoundException e) {
            // Assert
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Event Not Found: Event does not exist!", exceptionMsg);
    }

    // @Test
    // void addEvent_Success() {

    // String startDateTimeString = "2023-10-18T12:34:56.789Z";
    // String endDateTimeString = "2023-11-20T15:45:30.500Z";
    // // Arrange
    // EventDTO eventDTO = new EventDTO();
    // eventDTO.setName("event");
    // eventDTO.setStartDateTime(startDateTimeString);
    // eventDTO.setEndDateTime(endDateTimeString);
    // Event event = new Event();
    // event.setName("event");
    // DateTimeFormatter formatter =
    // DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    // event.setStartDateTime(LocalDateTime.parse(startDateTimeString, formatter));
    // event.setEndDateTime(LocalDateTime.parse(endDateTimeString, formatter));
    // when(eventRepository.save(event)).thenReturn(event);

    // // Act
    // Event result = eventService.addEvent(eventDTO);

    // // Assert
    // assertNotNull(result);
    // assertEquals(result, event);
    // verify(eventRepository).save(event);
    // }

    // @Test
    // void updateEvent_Success() {
    // // Arrange
    // Long eventId = 1L;
    // EventDTO eventDTO = new EventDTO();
    // Event existingEvent = new Event();
    // when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));

    // // Act
    // Event result = eventService.updateEvent(eventId, eventDTO);

    // // Assert
    // assertNotNull(result);
    // verify(eventRepository).saveAndFlush(existingEvent);
    // }

    // @Test
    // void updateEvent_EventNotFound_ThrowsEventNotFound() {
    // String exceptionMsg = "";
    // // Arrange
    // Long eventId = 1L;
    // EventDTO eventDTO = new EventDTO();
    // when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

    // try {
    // // Act
    // eventService.updateEvent(eventId, eventDTO);
    // } catch (EventNotFoundException e) {
    // // Assert
    // exceptionMsg = e.getMessage();
    // }

    // assertEquals(exceptionMsg, "Event Not Found: Event does not exist!");
    // }

    @Test
    void deleteEvent_Success() {
        // Arrange
        Organiser organiser = new Organiser();
        organiser.setId(1L);

        Event event = new Event();
        event.setId(1L);
        event.setOrganiser(organiser);

        List<Event> events = new ArrayList<>();
        events.add(event);
        organiser.setEvents(events);

        when(eventRepository.existsById(any(Long.class))).thenReturn(true);
        when(eventRepository.getReferenceById(any(Long.class))).thenReturn(event);

        // Act
        eventService.deleteEvent(event.getId());

        List<Event> emptyList = new ArrayList<>();

        // Assert
        assertEquals(emptyList, organiser.getEvents());
        verify(eventRepository).deleteById(1L);
    }

    @Test
    void deleteEvent_EventNotFound_ThrowsEventNotFound() {
        String exceptionMsg = "";
        // Arrange
        Long eventId = 1L;
        when(eventRepository.existsById(eventId)).thenReturn(false);

        try {
            // Act
            eventService.deleteEvent(eventId);
        } catch (EventNotFoundException e) {
            // Assert
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Event Not Found: Event does not exist!", exceptionMsg);
    }

    @Test
    void deleteEvent_OrganiserNULL_ThrowsEventNotFound() {
        String exceptionMsg = "";
        // Arrange
        Event event = new Event();
        event.setId(1L);
        event.setOrganiser(null);

        List<Event> events = new ArrayList<>();
        events.add(event);

        when(eventRepository.existsById(any(Long.class))).thenReturn(true);
        when(eventRepository.getReferenceById(any(Long.class))).thenReturn(event);

        try {
            // Act
            eventService.deleteEvent(1L);
        } catch (UserNotFoundException e) {
            // Assert
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("User Not Found: Organiser does not exist!", exceptionMsg);
    }

    @Test
    void getStudentByEventIdandEventRegistrationStatus__NULLStatus_Success_ReturnStudentList() {

        // Arrange
        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setEventId(1L);
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));
        // Act
        List<Student> result = eventService.getStudentByEventIdandEventRegistrationStatus(queryDTO);

        // Assert
        assertEquals(studentList, result);

    }

    @Test
    void getStudentByEventIdandEventRegistrationStatus__PENDINGStatus_Success_ReturnStudentList() {

        // Arrange
        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setEventId(1L);
        queryDTO.setStatus(Status.PENDING);
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));
        // Act
        List<Student> result = eventService.getStudentByEventIdandEventRegistrationStatus(queryDTO);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void getStudentByEventIdandEventRegistrationStatus__REJECTEDStatus_Success_ReturnStudentList() {

        // Arrange
        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setEventId(1L);
        queryDTO.setStatus(Status.REJECTED);
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));
        // Act
        List<Student> result = eventService.getStudentByEventIdandEventRegistrationStatus(queryDTO);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void getStudentByEventIdandEventRegistrationStatus__ACCEPTEDStatus_Success_ReturnStudentList() {

        // Arrange
        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setEventId(1L);
        queryDTO.setStatus(Status.ACCEPTED);
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));
        // Act
        List<Student> result = eventService.getStudentByEventIdandEventRegistrationStatus(queryDTO);

        // Assert
        assertEquals(1, result.size());
    }

    // public List<Student> getStudentByEventIdandEventRegistrationStatus(QueryDTO
    // queryDTO) {

    // Event event = getEvent(queryDTO.getEventId());
    // List<EventRegistration> registrations = event.getRegistrations();
    // List<Student> students = new ArrayList<>();

    // for (EventRegistration registration : registrations) {

    // if (registration.getStatus() == queryDTO.getStatus() || queryDTO.getStatus()
    // == null) {
    // students.add(registration.getStudent());
    // }
    // }

    // return students;
    // }
    // TODO: test

    // @Test
    // void getStudentByEventIdandEventRegistrationStatus() {
    // // Arrange
    // QueryDTO queryDTO = new QueryDTO();
    // Long eventId = 1L;
    // queryDTO.setEventId(eventId);
    // queryDTO.setStatus(EventRegistration.Status.CONFIRMED);

    // Event event = new Event();
    // EventRegistration registration = new EventRegistration();
    // registration.setStatus(EventRegistration.Status.CONFIRMED);
    // Student student = new Student();
    // registration.setStudent(student);
    // event.getRegistrations().add(registration);

    // when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

    // // Act
    // List<Student> result =
    // eventService.getStudentByEventIdandEventRegistrationStatus(queryDTO);

    // // Assert
    // assertEquals(1, result.size());
    // assertEquals(student, result.get(0));
    // }
}

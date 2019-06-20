package backfront.service;

import backfront.model.dto.ContactDto;

import java.util.List;

public interface ContactService {
    void createContact(ContactDto contactDto);

    void updateContact(Long id, ContactDto contactDto);

    void deleteContact(Long id);

    ContactDto getContact(Long id);

    List<ContactDto> getAllContacts();

    ContactDto getContactByName(String name);

    List <ContactDto> getContactByPhoneNumber(String phoneNumber);

}

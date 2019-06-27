package backfront.service;

import backfront.Exeption.PhoneNumberNotFoundException;
import backfront.Exeption.UserNotFoundException;
import backfront.model.dto.AddressDto;
import backfront.model.dto.ContactDto;
import backfront.model.entity.Address;
import backfront.model.entity.Phone_numbers;
import backfront.model.entity.Users;
import backfront.repository.AddressRepository;
import backfront.repository.PhoneNumberRepository;
import backfront.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
    public class ContactServiceImpl implements ContactService {

        @Autowired
        private PhoneNumberRepository phoneNumberRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private AddressRepository addressRepository;


        @Override
        @Transactional
        public void createContact(ContactDto contactDto) {
            Users user =Users.builder()
                    .fullName(contactDto.getFullName())
                    .email(contactDto.getEmail())
                    .localDateTime(LocalDateTime.now())
                    .build();

            List<Phone_numbers> phoneNumbers = getPhoneNumbersFromContactDto(contactDto, user);

            List <Address> addresses = createAddressesFromContactDto(contactDto, user);

            userRepository.save(user);
            phoneNumberRepository.saveAll(phoneNumbers);
            addressRepository.saveAll(addresses);
        }


        @Override
        @Transactional
        public void updateContact(Long id, ContactDto contactDto) {
            // TODO: 2019-06-11 Think how change phoneNumber and address without delete

            Users user = userRepository.findById(id).orElseThrow(() -> new UserPrincipalNotFoundException("User doesn't exist by id"));

/*
        List <PhoneNumber> phoneNumbers = phoneNumberRepository.findAllByUser(user);
        List <Address> addresses = addressRepository.findAllByUser(user);
        phoneNumberRepository.deleteAll(phoneNumbers);
        addressRepository.deleteAll(addresses);
*/

            Users userToChange = Users.builder()
                    .id(user.getId())
                    .fullName(contactDto.getFullName())
                    .email(contactDto.getEmail())
                    .localDateTime(user.getLocalDateTime())
                    .build();

            List <Phone_numbers> phoneNumbersToChange = getPhoneNumbersFromContactDto(contactDto, user);

            List <Address> addressesToChange = createAddressesFromContactDto(contactDto, user);

            userRepository.save(userToChange);
            phoneNumberRepository.saveAll(phoneNumbersToChange);
            addressRepository.saveAll(addressesToChange);

        }

        @Override
        @Transactional
        public void deleteContact(Long id) {
            Users user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User doesn't exist by id"));

            phoneNumberRepository.deleteAllByUser(user);
            addressRepository.deleteAllByUser(user);
            userRepository.delete(user);

        }

        @Override
        @Transactional(readOnly = true)
        public ContactDto getContact(Long id) {
            Users user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User doesn't exist by id"));

            List <Phone_numbers> phoneNumbers = phoneNumberRepository.findAllByUser(user);
            List <Address> addresses = addressRepository.findAllByUser(user);

            ContactDto contactDto = ContactDto.builder()
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phoneNumbers(phoneNumbers.stream().map(Phone_numbers::getPhoneNumber).collect(Collectors.toList()))
                    .addresses(addresses.stream().map(address -> convertAddressToAddressDto(address)).collect(Collectors.toList()))
                    .build();

            return contactDto;

        }

        @Override
        @Transactional(readOnly = true)
        public List <ContactDto> getAllContacts() {
            List <Long> ids = userRepository.getAllIds();

            return ids.stream().map(id -> getContact(id)).collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public ContactDto getContactByName(String name) {
            Users user = userRepository.findUserByFullName(name);
            return getContact(user.getId());
        }

        @Override
        @Transactional(readOnly = true)
        public List <ContactDto> getContactByPhoneNumber(String number) {
            List <Phone_numbers> phoneNumbers = phoneNumberRepository.findAllByPhoneNumber(number)
                    .orElseThrow(() -> new PhoneNumberNotFoundException("Can't find contact by this phone number"));

            List <ContactDto> contacts = new ArrayList<>();

            phoneNumbers.stream()
                    .map(phone -> phone.getUser())
                    .flatMap(users -> users.stream())
                    .forEach(user -> contacts.add(getContact(user.getId())));

            return contacts;
        }

        private AddressDto convertAddressToAddressDto(Address address) {
            return AddressDto.builder()
                    .country(address.getCountry())
                    .city(address.getCity())
                    .street(address.getStreet())
                    .apartment(address.getApartment())
                    .houseNumber(address.getHouseNumber())
                    .build();
        }

        private Address convertAddressDtoToAddress(Users user, AddressDto address) {
            Address addressFromDB = addressRepository.findBy(address.getCountry(),
                    address.getCity(), address.getStreet(), address.getHouseNumber(), address.getApartment())
                    .orElse(new Address(0l, LocalDateTime.now(), address.getCountry(),
                            address.getCity(), address.getStreet(), address.getHouseNumber(),
                            address.getApartment(), new ArrayList <>()));

            addressFromDB.getUser().add(user);

            return addressFromDB;
        }

        private List <Address> createAddressesFromContactDto(ContactDto contactDto, Users user) {
            return contactDto.getAddresses().stream()
                    .map(address -> convertAddressDtoToAddress(user, address)).collect(Collectors.toList());
        }

        private List <Phone_numbers> getPhoneNumbersFromContactDto(ContactDto contactDto, Users user) {
            return contactDto.getPhoneNumbers().stream()
                    .map(phone -> getPhoneNumberFromString(phone, user))
                    .collect(Collectors.toList());
        }

        private Phone_numbers getPhoneNumberFromString(String phone, Users user) {

            Phone_numbers phoneNumberFromDB = phoneNumberRepository.findPhoneNumberByPhoneNumber(phone)
                    .orElse(Phone_numbers.builder()
                            .id(0l)
                            .localDateTime(LocalDateTime.now())
                            .phoneNumber(phone)
                            .user(new ArrayList <>())
                            .build());

            phoneNumberFromDB.getUser().add(user);

            return phoneNumberFromDB;
        }
    }


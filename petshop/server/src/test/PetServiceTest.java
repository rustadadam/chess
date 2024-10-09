import dataaccess.MemoryDataAccess;
import exception.ResponseException;
import model.Pet;
import model.PetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.PetService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PetServiceTest {
    static final PetService service = new PetService(new MemoryDataAccess());

    @BeforeEach
    void clear() throws ResponseException {
        service.deleteAllPets();
    }

    @Test
    void addPet() throws ResponseException {
        var pet = new Pet(0, "joe", PetType.FISH);
        pet = service.addPet(pet);

        var pets = service.listPets();
        assertEquals(1, pets.size());
        assertTrue(pets.contains(pet));
    }

    @Test
    void listPets() throws ResponseException {
        List<Pet> expected = new ArrayList<>();
        expected.add(service.addPet(new Pet(0, "joe", PetType.FISH)));
        expected.add(service.addPet(new Pet(0, "sally", PetType.CAT)));
        expected.add(service.addPet(new Pet(0, "fido", PetType.DOG)));

        var actual = service.listPets();
        assertIterableEquals(expected, actual);
    }

    @Test
    void deletePet() throws ResponseException {
        List<Pet> expected = new ArrayList<>();
        var pet = service.addPet(new Pet(0, "joe", PetType.FISH));
        expected.add(service.addPet(new Pet(0, "sally", PetType.CAT)));
        expected.add(service.addPet(new Pet(0, "fido", PetType.DOG)));

        service.deletePet(pet.id());
        var actual = service.listPets();
        assertIterableEquals(expected, actual);
    }

    @Test
    void deleteAllPets() throws ResponseException {
        service.addPet(new Pet(0, "joe", PetType.FISH));
        service.addPet(new Pet(0, "sally", PetType.CAT));
        service.addPet(new Pet(0, "fido", PetType.DOG));

        service.deleteAllPets();
        assertEquals(0, service.listPets().size());
    }
}
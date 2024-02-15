package com.polarbookshop.catalogservice.domain;


import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BookValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {

        Book book = Book.of("1234567890", "Title", "Author", 9.90, "Manning");
        Set<ConstraintViolation<Book>> violations =  validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnDefinedButIncorrectThenValidationFails() {
        Book book = Book.of("a23456789", "Title", "Author", 9.90, "Manning");
        Set<ConstraintViolation<Book>> violations =  validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The ISBN format must be valid.");
    }
}

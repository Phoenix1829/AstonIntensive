package ru.phoenix.userservice.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.phoenix.userservice.contoller.UserController;
import ru.phoenix.userservice.dto.UserResponseDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class UserModelAssembler
        implements RepresentationModelAssembler<UserResponseDto, EntityModel<UserResponseDto>> {

    @Override
    public EntityModel<UserResponseDto> toModel(UserResponseDto dto) {

        return EntityModel.of(
                dto,

                linkTo(methodOn(UserController.class)
                        .getById(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(UserController.class)
                        .getAll())
                        .withRel("allUsers"),

                linkTo(methodOn(UserController.class)
                        .delete(dto.getId()))
                        .withRel("delete"),

                linkTo(methodOn(UserController.class)
                        .update(dto.getId(), null))
                        .withRel("update")
        );
    }
}
package dev.jenny.clara.role;

import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements InterfaceRoleService {

    private static final String DEFAULT_ROLE_NAME = "USER";

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public RoleEntity assignDefaultRole() {
        return repository.findByName(DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new IllegalStateException(
                        "El rol por defecto '" + DEFAULT_ROLE_NAME + "' no existe en base de datos"));
    }

}

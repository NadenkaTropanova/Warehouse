package com.salesregister.service;

import com.salesregister.domain.Operation;
import com.salesregister.repository.OperationRepository;
import com.salesregister.request.OperationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OperationService {
    private final OperationRepository repository;
    private final UserService userService;

    @Autowired
    public OperationService(OperationRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public List<Operation> getOperationsForCurrentUser() {
        return repository.findByUser(userService.getCurrentUser());
    }

    public List<Operation> getOperationsForCurrentUser(String query) {
        return repository.findByUserAndNameLike(userService.getCurrentUser(), "%" + query +"%");
    }

    public void addOperation(OperationRequest request) {
        Operation operation = new Operation();
        operation.setId(null);
        operation.setDate(new Date());
        operation.setName(request.getName());
        operation.setAmount(request.getAmount());
        operation.setDescription(request.getDescription());
        operation.setOperation(request.getOperation());
        operation.setPrice(request.getPrice());
        operation.setUser(userService.getCurrentUser());

        repository.save(operation);
    }

    public void deleteOperation(Long id) {
        repository.deleteById(id);
    }
}

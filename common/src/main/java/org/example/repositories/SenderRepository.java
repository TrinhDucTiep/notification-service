package org.example.repositories;

import org.example.models.Sender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SenderRepository extends JpaRepository<Sender, Integer> {
}

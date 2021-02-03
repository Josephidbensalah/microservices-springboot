package ma.omnishore.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.omnishore.clients.domain.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}

package com.ivoyant.kafkaemailevent.repository;

import com.ivoyant.kafkaemailevent.entity.Email;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends CassandraRepository<Email,String> {

}

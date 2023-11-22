package com.ivoyant.kafkaemailevent.repository;

import com.ivoyant.kafkaemailevent.entity.EmailAttach;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAttachRepository extends CassandraRepository<EmailAttach,String> {
}

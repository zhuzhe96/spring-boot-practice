package com.zhuzhe.h2service.repository;

import com.zhuzhe.h2service.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote,Long> {

}

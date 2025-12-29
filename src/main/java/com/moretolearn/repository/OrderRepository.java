package com.moretolearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moretolearn.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

}

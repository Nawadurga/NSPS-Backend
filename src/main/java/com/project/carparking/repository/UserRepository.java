package com.project.carparking.repository;

import com.project.carparking.entity.EnumRole;
import com.project.carparking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByPhoneNo(String phoneNo);
    Optional<User> findByPhoneNoContaining(String phoneNo);
    List<User> findByNameContaining(String name);

    List<User> findByRole(EnumRole role);

    @Query("SELECT DISTINCT u FROM User u JOIN u.vehicles v WHERE v.numberPlate LIKE %:numberPlate%")
    List<User> findByVehicleNumberPlateContaining(@Param("numberPlate") String numberPlate);

    Optional<User> findFirstByRoleOrderByCreatedAtAsc(EnumRole role);


}
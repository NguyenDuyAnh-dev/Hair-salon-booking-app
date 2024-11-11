package com.hairsalonbookingapp.hairsalon.repository;

import com.hairsalonbookingapp.hairsalon.entity.HairSalonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceRepository extends JpaRepository<HairSalonService, Long> {
    HairSalonService findHairSalonServiceByIdAndIsAvailableTrue(long id);
    List<HairSalonService> findHairSalonServicesByIsAvailableTrue();
    //List<HairSalonService> findHairSalonServicesByIsAvailableTrue();
    HairSalonService findHairSalonServiceById(long id);

    Page<HairSalonService> findHairSalonServicesByIsAvailableTrue(Pageable pageable);

    @Query(value = """
        SELECT hs.name, hs.description, hs.image, COUNT(sa.service_id) AS service_count
        FROM service_appointment sa
        JOIN hair_salon_service hs ON sa.service_id = hs.id
        GROUP BY hs.id, hs.name, hs.description, hs.image
        ORDER BY service_count DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findTop5MostSelectedServices();

}

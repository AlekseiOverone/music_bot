package com.smuraha.telegram.model.repo;

import com.smuraha.telegram.model.MyAudio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyAudioRepo extends CrudRepository<MyAudio,Integer> {
    Page<MyAudio> findAll(Pageable pageable);
//    @Query("select new MyAudio (id,duration,performer,title) from MyAudio")
    Page<MyAudio> findAllBySearchTitleContains(String title,Pageable pageable);
}

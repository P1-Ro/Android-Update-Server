package sk.p1ro.android_update_server.repository;

import org.springframework.data.repository.CrudRepository;
import sk.p1ro.android_update_server.entity.Apk;

public interface ApkRepository extends CrudRepository<Apk, String> {
}

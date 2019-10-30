
package com.SteelBlog.SteelBlog.repo;

import com.SteelBlog.SteelBlog.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<User, String> {

    User findByUsername(String username);

}

package study.falson.com.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import study.falson.com.Mode.GitModel;

/**
 * 作者： falcon on 2016/7/8.
 * 内容：
 */
public interface GitHubService {
    @GET("/users/{user}/repos")
    Call<List<GitModel>> listRepos(@Path("user") String user);
}

package recommend.service;

import org.springframework.stereotype.Service;
import recommend.service.api.ITestService;

/**
 * Created by ouduobiao on 15/6/21.
 */
@Service
public class TestService implements ITestService{
    @Override
    public String getName() {
        return "ouduobiao";
    }
}

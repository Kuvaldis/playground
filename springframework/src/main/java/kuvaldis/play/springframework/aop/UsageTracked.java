package kuvaldis.play.springframework.aop;

public interface UsageTracked {

    void incrementUseCount();

    int getCount();
}

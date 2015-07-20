package kuvaldis.play.springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class HelloWorldBatchConfiguration {

    private static final Logger log = LoggerFactory.getLogger(HelloWorldBatchConfiguration.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private static final List<Integer> additionOutput = new ArrayList<>();
    private static final List<Integer> multiplicationOutput = new ArrayList<>();

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> null)
                .build();
    }

    @Bean
    public Job job1(final Step step1) throws Exception {
        return jobBuilderFactory.get("job1")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    @Qualifier("additionReader")
    public ItemReader<Integer> additionReader() {
        return new ListItemReader<>(Arrays.asList(1, 10, 50));
    }

    @Bean
    @Qualifier("multiplicationReader")
    public ItemReader<Integer> multiplicationReader() {
        return () -> {
            if (!additionOutput.isEmpty()) {
                return additionOutput.remove(0);
            }
            return null;
        };
    }

    @Bean
    @Qualifier("additionWriter")
    public ListItemWriter<Integer> additionWriter() {
        return new ListItemWriter<>();
        // you can save all the items like this, add ExecutionContextPromotionListener with the same key
        // to the first (addition) step and then retrieve it from stepExecution.getJobExecution().getExecutionContext().get("someKey")
//        return new ItemWriter<Integer>() {
//
//            private StepExecution stepExecution;
//
//            @Override
//            public void write(List<? extends Integer> items) throws Exception {
//                ExecutionContext stepContext = this.stepExecution.getExecutionContext();
//                stepContext.put("someKey", someObject);
//            }
//
//            @BeforeStep
//            public void saveStepExecution(StepExecution stepExecution) {
//                this.stepExecution = stepExecution;
//            }
//        };
    }

    @Bean
    @Qualifier("multiplicationWriter")
    public ListItemWriter<Integer> multiplicationWriter() {
        return new ListItemWriter<>();
    }

    @Bean
    @Qualifier("additionWriterListener")
    public ItemWriteListener<Integer> additionWriterListener() {
        return new ItemListenerSupport<Integer, Integer>() {
            @Override
            public void afterProcess(Integer item, Integer result) {
                additionOutput.add(result);
                log.info(item.toString());
            }
        };
    }

    @Bean
    @Qualifier("multiplicationWriterListener")
    public ItemWriteListener<Integer> multiplicationWriterListener() {
        return new ItemListenerSupport<Integer, Integer>() {
            @Override
            public void afterWrite(final List<? extends Integer> item) {
                multiplicationOutput.addAll(item);
                log.info(item.toString());
            }
        };
    }

    @Bean
    public Step addition(final @Qualifier("additionReader") ItemReader<Integer> additionReader,
                         final @Qualifier("additionWriter") ItemWriter<Integer> additionWriter,
                         final @Qualifier("additionWriterListener") ItemWriteListener<Integer> additionWriterListener) {
        return stepBuilderFactory.get("addition")
                .<Integer, Integer>chunk(2)
                .listener(additionWriterListener)
                .reader(additionReader)
                .processor(item -> item + 1)
                .writer(additionWriter)
                .build();
    }

    @Bean
    public Step multiplication(final @Qualifier("multiplicationReader") ItemReader<Integer> multiplicationReader,
                               final @Qualifier("multiplicationWriter") ItemWriter<Integer> multiplicationWriter,
                               final @Qualifier("multiplicationWriterListener") ItemWriteListener<Integer> multiplicationWriterListener) {
        return stepBuilderFactory.get("multiplication")
                .<Integer, Integer>chunk(2)
                .listener(multiplicationWriterListener)
                .reader(multiplicationReader)
                .processor(item -> item * 2)
                .writer(multiplicationWriter)
                .build();
    }

    // or we can use composite item processor
    @Bean
    public Job operationsJob(final Step addition, final Step multiplication) {
        return jobBuilderFactory.get("operationsJob")
                .start(addition)
                .next(multiplication)
                .build();
    }

    public static void main(String[] args) {
        final ConfigurableApplicationContext run = SpringApplication.run(HelloWorldBatchConfiguration.class, args);
        log.info(multiplicationOutput.toString());
        System.exit(SpringApplication.exit(run));
    }
}

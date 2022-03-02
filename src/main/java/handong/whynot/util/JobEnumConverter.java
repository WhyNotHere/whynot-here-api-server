package handong.whynot.util;

import org.springframework.core.convert.converter.Converter;

import handong.whynot.dto.job.JobType;
import org.springframework.stereotype.Component;

@Component
public class JobEnumConverter implements Converter<String, JobType> {

    @Override
    public JobType convert(String source) {
        return JobType.valueOf(source.toUpperCase());
    }
}
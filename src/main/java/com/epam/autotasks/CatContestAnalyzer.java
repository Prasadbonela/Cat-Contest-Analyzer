package com.epam.autotasks;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class CatContestAnalyzer {

    public static final Integer DEFAULT_VALUE = -1;

    private Integer getContestResultSum(Cat cat) {
        return cat.getContestResult() != null ? cat.getContestResult().getSum() : 0;
    }

    public Integer getMaxResult(List<Cat> cats) {
        return cats.stream()
                .map(this::getContestResultSum)
                .max(Integer::compare)
                .orElse(DEFAULT_VALUE);
    }

    public Integer getMinResult(List<Cat> cats) {
        return cats.stream()
                .map(this::getContestResultSum)
                .filter(result -> result > 0)
                .min(Integer::compare)
                .orElse(DEFAULT_VALUE);
    }

    public OptionalDouble getAverageResultByBreed(List<Cat> cats, Cat.Breed breed) {
        return cats.stream()
                .filter(cat -> cat.getBreed() == breed && cat.getContestResult() != null)
                .mapToInt(this::getContestResultSum)
                .average();
    }

    public Optional<Cat> getWinner(List<Cat> cats) {
        return cats.stream()
                .filter(cat -> cat.getContestResult() != null)
                .max(Comparator.comparingInt(this::getContestResultSum));
    }

    public List<Cat> getThreeLeaders(List<Cat> cats) {
        return cats.stream()
                .filter(cat -> cat.getContestResult() != null)
                .sorted(Comparator.comparingInt(this::getContestResultSum).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public boolean validateResultSumNotNull(List<Cat> cats) {
        return cats.stream()
                .allMatch(cat -> cat.getContestResult() != null && cat.getContestResult().getSum() > 0);
    }

    public boolean validateAllResultsSet(List<Cat> cats) {
        return cats.stream()
                .allMatch(cat -> cat.getContestResult() != null &&
                        cat.getContestResult().getRunning() != 0 &&
                        cat.getContestResult().getJumping() != 0 &&
                        cat.getContestResult().getPurring() != 0);
    }

    public Optional<Cat> findAnyWithAboveAverageResultByBreed(List<Cat> cats, Cat.Breed breed) {
        OptionalDouble average = getAverageResultByBreed(cats, breed);
        if (average.isEmpty()) {
            return Optional.empty();
        }
        double avg = average.getAsDouble();
        return cats.stream()
                .filter(cat -> cat.getBreed() == breed && cat.getContestResult() != null)
                .filter(cat -> getContestResultSum(cat) > avg)
                .findAny();
    }
}

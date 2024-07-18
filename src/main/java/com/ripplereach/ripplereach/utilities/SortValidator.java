package com.ripplereach.ripplereach.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.data.domain.Sort;

public class SortValidator {

  private static final Logger logger = Logger.getLogger(SortValidator.class.getName());

  public static List<Sort.Order> validateSort(String sortParam, List<String> allowedProperties) {
    List<Sort.Order> orders = new ArrayList<>();

    if (sortParam == null || sortParam.isEmpty()) {
      return orders;
    }

    String[] sortPairs = sortParam.split(",");
    for (int i = 0; i < sortPairs.length; i += 2) {
      if (i + 1 < sortPairs.length) {
        String property = sortPairs[i];
        String directionStr = sortPairs[i + 1];

        try {
          Sort.Direction direction = Sort.Direction.fromString(directionStr);
          if (allowedProperties.contains(property)) {
            orders.add(new Sort.Order(direction, property));
          } else {
            logger.log(Level.WARNING, "Invalid sort property: {0}", property);
          }
        } catch (IllegalArgumentException e) {
          logger.log(Level.WARNING, "Invalid sort direction: {0}", directionStr);
        }
      }
    }
    return orders;
  }
}

package net.unicon.grouper.changelog.esb.publisher;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Implementation which replaces parts of the provided group name with values of configured regex replacement definition in Spring Expression
 * Language.
 */
public class SpelRegexReplacementBasedRoutingKeyCreator extends AbstractGroupNameExtractingRoutingKeyCreator {

    private final Expression parsedExpression;

    public SpelRegexReplacementBasedRoutingKeyCreator(boolean replaceColonsWithPeriods, String replacementDefinitionString) {
        super(replaceColonsWithPeriods);
        this.parsedExpression = new SpelExpressionParser().parseExpression(replacementDefinitionString);
    }

    @Override
    public String createRoutingKeyFromGroupName(String groupName) {
        if (groupName == null) {
            return null;
        }
        return String.class.cast(this.parsedExpression.getValue(new StandardEvaluationContext(groupName)));
    }
}

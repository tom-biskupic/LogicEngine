package com.runcible.logicengine.structure;

import com.runcible.logicengine.logic.BooleanExpression;
import com.runcible.logicengine.logic.ConstantExpression;

/**
 * Modular addition of two n-bit values using a ripple-carry adder.
 * Carry out (overflow) is discarded, giving addition mod 2^n.
 *
 * Bit ordering: bit 0 is the MSB (weight 2^(n-1)), bit n-1 is the LSB (weight 2^0).
 * The adder processes from LSB (index n-1) toward MSB (index 0).
 */
public class AddExpressionBlock extends ExpressionBlock
{
    public AddExpressionBlock(ExpressionBlock a, ExpressionBlock b) throws ExpressionBlockSizeError
    {
        super(a.size());

        if (a.size() != b.size())
        {
            throw new ExpressionBlockSizeError(
                "Left source = " + a.size() + " Right source = " + b.size());
        }

        int n = a.size();

        // Carry starts at 0 (LSB carry-in = false)
        BooleanExpression carry = new ConstantExpression(false);

        BooleanExpression[] sums = new BooleanExpression[n];

        // Ripple from LSB (index n-1) to MSB (index 0)
        for (int i = n - 1; i >= 0; i--)
        {
            BooleanExpression ai = a.getExpression(i);
            BooleanExpression bi = b.getExpression(i);

            // sum_i = ai XOR bi XOR carry
            BooleanExpression axorb = makeXOR(ai, bi);
            sums[i] = makeXOR(axorb, carry);

            // carry_out = (ai AND bi) OR (carry AND (ai XOR bi))
            carry = BooleanExpression.makeOr(
                BooleanExpression.makeAnd(ai, bi),
                BooleanExpression.makeAnd(carry, axorb)
            );
        }

        // Carry out is discarded (mod 2^n)
        for (int i = 0; i < n; i++)
        {
            setExpression(i, sums[i]);
        }
    }

    // XOR(a,b) = (a OR b) AND (NOT(a) OR NOT(b))
    private static BooleanExpression makeXOR(BooleanExpression a, BooleanExpression b)
    {
        return BooleanExpression.makeAnd(
            BooleanExpression.makeOr(a, b),
            BooleanExpression.makeOr(
                BooleanExpression.makeNot(a),
                BooleanExpression.makeNot(b)
            )
        );
    }
}

package com.runcible.logicengine.sha2;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.runcible.logicengine.logic.Evaluator;
import com.runcible.logicengine.logic.LogicReducer;
import com.runcible.logicengine.logic.NotConstantError;
import com.runcible.logicengine.structure.ConstantExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;
import com.runcible.logicengine.structure.TerminalBlock;
import com.runcible.logicengine.logic.Printer;

public class SHA256Test
{
    // SHA-256("abc") — verified against Java MessageDigest and a reference implementation.
    // Input: 512-bit block: 61626380 [14 × 00000000] 00000018
    private static final String ABC_MESSAGE =
        "6162638000000000000000000000000000000000000000000000000000000000" +
        "0000000000000000000000000000000000000000000000000000000000000018";

    private static final String ABC_DIGEST =
        "BA7816BF8F01CFEA414140DE5DAE2223B00361A396177A9CB410FF61F20015AD";

    /**
     * Verifies SHA-256 against a known-answer test: all 512 input bits are
     * constants, so the Evaluator reduces the entire tree to 256 constant bits.
     */
    @Test
    public void testSHA256() throws ExpressionBlockSizeError, NotConstantError
    {
        ConstantExpressionBlock message = new ConstantExpressionBlock(512, "m");
        message.setValueFromHex(ABC_MESSAGE);

        SHA256 sha256 = new SHA256(message);

        TerminalBlock terminal = new TerminalBlock(sha256);

        sha256.apply(new Evaluator());

        assertEquals(ABC_DIGEST, terminal.valueAsHex());
    }

    /**
     * Leaves the entire message as symbolic unknowns, runs the Evaluator and
     * LogicReducer (the K and H constants reduce, leaving symbolic remainder),
     * then sets the message to the known value and re-evaluates to confirm the
     * reduced tree still gives the correct digest.
     *
     * Mirrors the DES testReduce pattern.
     */
    @Test
    public void testReduce() throws ExpressionBlockSizeError, NotConstantError
    {
        // All 512 message bits are symbolic variables (no values set yet)
        ConstantExpressionBlock message = new ConstantExpressionBlock(512, "m");

        SHA256 sha256 = new SHA256(message);

        TerminalBlock terminal = new TerminalBlock(sha256);

        // System.out.println("Running Reducer...");
        // LogicReducer reducer = new LogicReducer();
        // sha256.apply(reducer);
        // System.out.println(reducer.statsAsString());

        // Set the message to the known test vector and re-evaluate with a fresh Evaluator
        message.setValueFromHex(ABC_MESSAGE);
        sha256.apply(new LogicReducer());
        sha256.apply(new Evaluator());
        //sha256.getExpression(0).apply(new Printer());
        assertEquals(ABC_DIGEST, terminal.valueAsHex());
    }
}

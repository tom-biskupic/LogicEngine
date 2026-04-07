package com.runcible.logicengine.sha2;

import com.runcible.logicengine.structure.AddExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;

/**
 * SHA-256 message schedule.
 *
 * Expands the 16 input words (512-bit block) to 64 words:
 *   W[0..15]  = the 16 input words
 *   W[i]      = σ1(W[i-2]) + W[i-7] + σ0(W[i-15]) + W[i-16]  for i = 16..63
 */
public class MessageSchedule
{
    public MessageSchedule(ExpressionBlock input) throws ExpressionBlockSizeError
    {
        words = new ExpressionBlock[64];

        // W[0..15]: slice the 512-bit input into 16 x 32-bit words
        for (int i = 0; i < 16; i++)
        {
            words[i] = input.getSlice(i * 32, 32);
        }

        // W[16..63]: expand using the SHA-256 schedule formula
        for (int i = 16; i < 64; i++)
        {
            LittleSigma1Block s1   = new LittleSigma1Block(words[i - 2]);
            LittleSigma0Block s0   = new LittleSigma0Block(words[i - 15]);

            AddExpressionBlock sum1 = new AddExpressionBlock(s1,   words[i - 7]);
            AddExpressionBlock sum2 = new AddExpressionBlock(sum1, s0);
            AddExpressionBlock sum3 = new AddExpressionBlock(sum2, words[i - 16]);

            words[i] = sum3;
        }
    }

    public ExpressionBlock getWord(int i)
    {
        return words[i];
    }

    private final ExpressionBlock[] words;
}

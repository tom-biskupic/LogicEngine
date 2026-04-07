package com.runcible.logicengine.sha2;

import com.runcible.logicengine.structure.AddExpressionBlock;
import com.runcible.logicengine.structure.ConstantExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;

/**
 * SHA-256 compression function modelled as a boolean logic expression tree.
 *
 * Input:  a 512-bit message block (ExpressionBlock of 512 bits).
 *         Bits may be symbolic (unknown variables) or set to known constants.
 * Output: a 256-bit hash value (this ExpressionBlock, bits 0-255).
 *
 * The algorithm follows FIPS 180-4. All 64 round constants and the 8 initial
 * hash values are constant expressions that collapse to known bit values.
 */
public class SHA256 extends ExpressionBlock
{
    // SHA-256 initial hash values H0..H7 (fractional parts of sqrt of first 8 primes)
    private static final String[] INITIAL_HASH_VALUES = {
        "6a09e667", "bb67ae85", "3c6ef372", "a54ff53a",
        "510e527f", "9b05688c", "1f83d9ab", "5be0cd19"
    };

    // SHA-256 round constants K0..K63 (fractional parts of cbrt of first 64 primes)
    private static final String[] ROUND_CONSTANTS = {
        "428a2f98", "71374491", "b5c0fbcf", "e9b5dba5",
        "3956c25b", "59f111f1", "923f82a4", "ab1c5ed5",
        "d807aa98", "12835b01", "243185be", "550c7dc3",
        "72be5d74", "80deb1fe", "9bdc06a7", "c19bf174",
        "e49b69c1", "efbe4786", "0fc19dc6", "240ca1cc",
        "2de92c6f", "4a7484aa", "5cb0a9dc", "76f988da",
        "983e5152", "a831c66d", "b00327c8", "bf597fc7",
        "c6e00bf3", "d5a79147", "06ca6351", "14292967",
        "27b70a85", "2e1b2138", "4d2c6dfc", "53380d13",
        "650a7354", "766a0abb", "81c2c92e", "92722c85",
        "a2bfe8a1", "a81a664b", "c24b8b70", "c76c51a3",
        "d192e819", "d6990624", "f40e3585", "106aa070",
        "19a4c116", "1e376c08", "2748774c", "34b0bcb5",
        "391c0cb3", "4ed8aa4a", "5b9cca4f", "682e6ff3",
        "748f82ee", "78a5636f", "84c87814", "8cc70208",
        "90befffa", "a4506ceb", "bef9a3f7", "c67178f2"
    };

    public SHA256(ExpressionBlock input) throws ExpressionBlockSizeError
    {
        super(256);

        // Build round constant blocks (all known constants)
        ConstantExpressionBlock[] kBlocks = new ConstantExpressionBlock[64];
        for (int i = 0; i < 64; i++)
        {
            kBlocks[i] = new ConstantExpressionBlock(32, "K" + i + "_");
            kBlocks[i].setValueFromHex(ROUND_CONSTANTS[i]);
        }

        // Build initial hash value blocks (all known constants)
        ConstantExpressionBlock[] hBlocks = new ConstantExpressionBlock[8];
        for (int i = 0; i < 8; i++)
        {
            hBlocks[i] = new ConstantExpressionBlock(32, "H" + i + "_");
            hBlocks[i].setValueFromHex(INITIAL_HASH_VALUES[i]);
        }

        // Build the message schedule W[0..63]
        MessageSchedule schedule = new MessageSchedule(input);

        // Initialise state (a,b,c,d,e,f,g,h) = (H0..H7)
        ExpressionBlock state = new ExpressionBlock(256);
        for (int i = 0; i < 8; i++)
        {
            state.setExpressions(hBlocks[i], i * 32);
        }

        // Run 64 compression rounds
        Round[] rounds = new Round[64];
        rounds[0] = new Round(state, kBlocks[0], schedule.getWord(0));
        for (int i = 1; i < 64; i++)
        {
            rounds[i] = new Round(rounds[i - 1], kBlocks[i], schedule.getWord(i));
        }

        // Add initial hash values to final state: H[i] + finalState[i]
        ExpressionBlock finalState = rounds[63];
        for (int i = 0; i < 8; i++)
        {
            ExpressionBlock stateWord = finalState.getSlice(i * 32, 32);
            AddExpressionBlock sum    = new AddExpressionBlock(stateWord, hBlocks[i]);
            setExpressions(sum, i * 32);
        }
    }
}

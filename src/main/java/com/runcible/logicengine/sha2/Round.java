package com.runcible.logicengine.sha2;

import com.runcible.logicengine.structure.AddExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;

/**
 * One SHA-256 compression round.
 *
 * State layout: 256 bits = 8 x 32-bit words [a, b, c, d, e, f, g, h]
 * stored MSB-first with a at bits 0-31, b at 32-63, ..., h at 224-255.
 *
 * Round update:
 *   T1 = h + Σ1(e) + Ch(e,f,g) + K[i] + W[i]
 *   T2 = Σ0(a) + Maj(a,b,c)
 *   new state: (T1+T2, a, b, c, d+T1, e, f, g)
 */
public class Round extends ExpressionBlock
{
    public Round(ExpressionBlock state, ExpressionBlock kWord, ExpressionBlock wWord)
        throws ExpressionBlockSizeError
    {
        super(256);

        ExpressionBlock a = state.getSlice(0,   32);
        ExpressionBlock b = state.getSlice(32,  32);
        ExpressionBlock c = state.getSlice(64,  32);
        ExpressionBlock d = state.getSlice(96,  32);
        ExpressionBlock e = state.getSlice(128, 32);
        ExpressionBlock f = state.getSlice(160, 32);
        ExpressionBlock g = state.getSlice(192, 32);
        ExpressionBlock h = state.getSlice(224, 32);

        // T1 = h + Σ1(e) + Ch(e,f,g) + K[i] + W[i]
        BigSigma1Block     sigma1e = new BigSigma1Block(e);
        ChBlock            ch      = new ChBlock(e, f, g);
        AddExpressionBlock t1a     = new AddExpressionBlock(h,   sigma1e);
        AddExpressionBlock t1b     = new AddExpressionBlock(t1a, ch);
        AddExpressionBlock t1c     = new AddExpressionBlock(t1b, kWord);
        AddExpressionBlock t1      = new AddExpressionBlock(t1c, wWord);

        // T2 = Σ0(a) + Maj(a,b,c)
        BigSigma0Block     sigma0a = new BigSigma0Block(a);
        MajBlock           maj     = new MajBlock(a, b, c);
        AddExpressionBlock t2      = new AddExpressionBlock(sigma0a, maj);

        // new a = T1 + T2
        AddExpressionBlock newA = new AddExpressionBlock(t1, t2);
        // new e = d + T1
        AddExpressionBlock newE = new AddExpressionBlock(d, t1);

        // Pack new state: (newA, a, b, c, newE, e, f, g)
        setExpressions(newA, 0);
        setExpressions(a,   32);
        setExpressions(b,   64);
        setExpressions(c,   96);
        setExpressions(newE, 128);
        setExpressions(e,   160);
        setExpressions(f,   192);
        setExpressions(g,   224);
    }
}

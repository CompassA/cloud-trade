package org.study.trade.commodity.sequence;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.study.trade.commodity.mapper.data.SequencePO;
import org.study.trade.common.sequence.SequenceException;

import java.util.concurrent.CountDownLatch;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tomato
 * Created on 2021.08.27
 */
@RunWith(PowerMockRunner.class)
public class SequenceTest {

    @InjectMocks
    private DbSequenceGenerator mockDbSequenceGenerator;

    @Mock
    private SequencePOMapper mockMapper;

    private SequencePO mockSequencePO;

    @Before
    public void init() {
        mockSequencePO = new SequencePO(1, "mock", 1L, 10L, null, null);
    }

    @After
    public void destroy() {
        mockSequencePO = new SequencePO(1, "mock", 1L, 10L, null, null);
    }

    /**
     * 测试对内存中的sequence更新是否正常
     */
    @Test
    public void initTest() throws Exception {
        when(mockMapper.selectByPrimaryKey(anyInt())).thenReturn(mockSequencePO);
        when(mockMapper.casUpdateValue(anyInt(), anyLong(), anyLong())).thenReturn(1);

        int threadNum = 30;
        CountDownLatch subWait = new CountDownLatch(1);
        CountDownLatch mainWait = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; ++i) {
            new Thread(() -> {
                try {
                    subWait.await();
                    mockDbSequenceGenerator.nextValue();
                    mainWait.countDown();
                } catch (SequenceException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        subWait.countDown();
        mainWait.await();

        verify(mockMapper, times(3)).selectByPrimaryKey(anyInt());
        verify(mockMapper, times(3)).casUpdateValue(anyInt(), anyLong(), anyLong());
    }

    /**
     * 测试溢出的情况
     */
    @Test
    public void longOverflowTest() {
        mockSequencePO.setValue(Long.MAX_VALUE);
        when(mockMapper.selectByPrimaryKey(anyInt())).thenReturn(mockSequencePO);
        when(mockMapper.casUpdateValue(anyInt(), anyLong(), anyLong())).thenReturn(1);

        boolean hasException = false;
        try {
            mockDbSequenceGenerator.nextValue();
        } catch (SequenceException e) {
            e.printStackTrace();
            hasException = true;
        }

        Assert.assertTrue(hasException);
    }
}

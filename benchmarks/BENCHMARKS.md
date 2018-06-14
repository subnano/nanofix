### Still using SimpleDataFormat
```
....[Thread state: RUNNABLE]........................................................................
 50.0%  60.2% java.net.SocketInputStream.socketRead0
 10.6%  12.8% java.text.SimpleDateFormat.format
  9.1%  11.0% net.nanofix.message.NanoFixMessageBench.encodeHeartbeat
  4.7%   5.6% java.text.SimpleDateFormat.subFormat
  3.9%   4.7% java.text.DateFormatSymbols.<init>
  2.7%   3.3% java.text.DecimalFormat.format
  1.0%   1.2% java.nio.charset.CharsetEncoder.replaceWith
  0.4%   0.5% sun.nio.cs.US_ASCII.newEncoder
  0.2%   0.3% java.util.concurrent.ConcurrentHashMap.get
  0.2%   0.2% java.util.Arrays.copyOf
  0.2%   0.3% <other>

....[Thread state: WAITING].........................................................................
 16.9% 100.0% sun.misc.Unsafe.park

# Run complete. Total time: 00:00:38

Benchmark                                                             Mode  Cnt     Score     Error   Units
NanoFixMessageBench.encodeHeartbeat                                   avgt    9   747.310 ±  58.999   ns/op
NanoFixMessageBench.encodeHeartbeat:·gc.alloc.rate                    avgt    9   887.177 ±  71.839  MB/sec
NanoFixMessageBench.encodeHeartbeat:·gc.alloc.rate.norm               avgt    9  1040.623 ±   0.034    B/op
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Eden_Space           avgt    9   890.093 ± 139.156  MB/sec
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Eden_Space.norm      avgt    9  1044.150 ± 137.695    B/op
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Survivor_Space       avgt    9     0.076 ±   0.074  MB/sec
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Survivor_Space.norm  avgt    9     0.091 ±   0.091    B/op
NanoFixMessageBench.encodeHeartbeat:·gc.count                         avgt    9    68.000            counts
NanoFixMessageBench.encodeHeartbeat:·gc.time                          avgt    9    66.000                ms
NanoFixMessageBench.encodeHeartbeat:·stack                            avgt            NaN               ---
```

### Started using DateTimeEncoder
```
....[Thread state: RUNNABLE]........................................................................
 50.0%  60.1% java.net.SocketInputStream.socketRead0
 26.9%  32.3% net.nanofix.time.UtcDateTimeEncoder.encodeTime
  4.8%   5.8% net.nanofix.message.NanoFixMessageBench.encodeHeartbeat
  1.4%   1.7% net.nanofix.message.MessageHeader.populateBuffer
  0.0%   0.0% io.nano.core.buffer.ByteBufferUtil.asByteArray
  0.0%   0.0% net.nanofix.util.ByteArrayUtil.as1ByteArray

....[Thread state: WAITING].........................................................................
 16.9% 100.0% sun.misc.Unsafe.park

# Run complete. Total time: 00:00:38

Benchmark                                                             Mode  Cnt    Score    Error   Units
NanoFixMessageBench.encodeHeartbeat                                   avgt    9  928.005 ± 16.472   ns/op
NanoFixMessageBench.encodeHeartbeat:·gc.alloc.rate                    avgt    9  113.624 ± 36.933  MB/sec
NanoFixMessageBench.encodeHeartbeat:·gc.alloc.rate.norm               avgt    9  166.012 ± 54.248    B/op
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Eden_Space           avgt    9  120.924 ± 15.547  MB/sec
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Eden_Space.norm      avgt    9  176.572 ± 21.772    B/op
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Survivor_Space       avgt    9    0.053 ±  0.058  MB/sec
NanoFixMessageBench.encodeHeartbeat:·gc.churn.PS_Survivor_Space.norm  avgt    9    0.077 ±  0.085    B/op
NanoFixMessageBench.encodeHeartbeat:·gc.count                         avgt    9   35.000           counts
NanoFixMessageBench.encodeHeartbeat:·gc.time                          avgt    9   31.000               ms
NanoFixMessageBench.encodeHeartbeat:·stack                            avgt           NaN              ---
```

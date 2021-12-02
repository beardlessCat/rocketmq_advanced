# 一、消息丢失的情况

![](https://tcs.teambition.net/storage/312cefecd848aa27a85a426bf6a7e83ca16f?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTYzOTAyNjMzNSwiaWF0IjoxNjM4NDIxNTM1LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmNlZmVjZDg0OGFhMjdhODVhNDI2YmY2YTdlODNjYTE2ZiJ9.PFSNiYJcaj1FSwxLEAeMv2mkJpiskFI1DlxFB-Czj7U&download=image.png "")

## 1.消息发送失败

在生产者往broker中发送消息时，出现网抖动或网络故障，导致通讯失败，使得消息无法发送到broker中。

![](https://tcs.teambition.net/storage/312c82687b663ec348cafd51b4219be96eb8?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTYzOTAyNjMzNSwiaWF0IjoxNjM4NDIxNTM1LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmM4MjY4N2I2NjNlYzM0OGNhZmQ1MWI0MjE5YmU5NmViOCJ9.RjRiXY_u3_C0KX9JHf9qKz9YfoFDx1z-FdSTU-GCsmI&download=image.png "")

## 2.消息发送成功，broker消息存储消息失败

你的消息写入MQ之后，其实MQ可能仅仅是把这个消息给写入到page cache里，也就是操作系统自己管理的一个缓冲区，此时出现broker宕机，导致消息丢失。

再极端的情况下，消息落入磁盘，但是磁盘坏掉了。

![](https://tcs.teambition.net/storage/312c5665cc5cc15d45b04b590f268301520c?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTYzOTAyNjMzNSwiaWF0IjoxNjM4NDIxNTM1LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmM1NjY1Y2M1Y2MxNWQ0NWIwNGI1OTBmMjY4MzAxNTIwYyJ9.Qzz30xqv3EqbGP2J606JiTvcI19BKpbGuolpJJpCbsg&download=image.png "")

## 3.消费者消息丢失

消费者提交了消费的offset，但是业务处理还未处理完，消费者宕机，导致消费的offset已经提交，但是业务处理并未走完。

![](https://tcs.teambition.net/storage/312c7432b3b83302aff75e59fe41369c6f63?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTYzOTAyNjMzNSwiaWF0IjoxNjM4NDIxNTM1LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmM3NDMyYjNiODMzMDJhZmY3NWU1OWZlNDEzNjljNmY2MyJ9.EaQLwFM9j4RaUFWjgN-Pk1A-DpxQfANajqxBZLROLfo&download=image.png "")

# 二、消息可靠性投递方案

## 1.解决消息推送丢失——事务消息

### 1.1发送half消息

![](https://tcs.teambition.net/storage/312c7a047c37e14f41eb88727006054bea1c?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IjVmYWZhYjk1YTNkYmY4NTljODhkN2ZiNiIsImV4cCI6MTYzODQyNjczMSwiaWF0IjoxNjM4NDIzMTMxLCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmM3YTA0N2MzN2UxNGY0MWViODg3MjcwMDYwNTRiZWExYyJ9.KlWRpJ3znP5oaLhoQ622vyxdsgbRaQJH-_aqSEkuOvQ&download=image.png "")

消费者先发一个half消息给MQ以及收到他的成功的响应，初步先跟MQ做个联系和沟通。大概这个意思就是说，确认一下MQ还活着，MQ也知道你后续可能想发送一条很关键的不希望丢失的消息。

**所谓的half消息实际就是订单支付成功的消息，只不过他的状态是half。也就是他是half状态的时候，其他消费服务是看不见他的，没法获取到这条消息，必须等到订单系统执行commit请求，消息被commit之后，红包系统才可以看到和获取这条消息进行后续处理。**

- **half消息写入失败（收到失败响应）**

half消息发送失败可能MQ就挂了，或者这个时候网络就是故障了，所以导致你的half消息都没发送成功，总之你现在肯定没法跟MQ通信了，消费者需要根据自身的业务做一些处理，例如事务回滚之类的。

![](https://tcs.teambition.net/storage/312c8044bc65fc1008e16a0221235ccec15a?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IjVmYWZhYjk1YTNkYmY4NTljODhkN2ZiNiIsImV4cCI6MTYzODQyNjc4OSwiaWF0IjoxNjM4NDIzMTg5LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmM4MDQ0YmM2NWZjMTAwOGUxNmEwMjIxMjM1Y2NlYzE1YSJ9.7sy9qP8XPbhS-r6KUKcyXeZdLSUuKVAeeLQG-EOHgoc&download=image.png "")

- **half消息写入成功（收到成功响应）**

half消息写入成功，则执行一些业务逻辑，例如更新订单数据

![](https://tcs.teambition.net/storage/312c6614dd266c3e5f74158fc8084e564cb8?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IjVmYWZhYjk1YTNkYmY4NTljODhkN2ZiNiIsImV4cCI6MTYzODQyNjgzOSwiaWF0IjoxNjM4NDIzMjM5LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmM2NjE0ZGQyNjZjM2U1Zjc0MTU4ZmM4MDg0ZTU2NGNiOCJ9.FemoPcI4PRSJbnVF00l8IJzQv41ddTWEUK4XIXuYePg&download=image.png "")

此时如果更新订单数据失败，则需发送rollback请求，删除mq中的half消息。

![](https://tcs.teambition.net/storage/312cdb048e5842954208403ebd20da1aa8ef?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IjVmYWZhYjk1YTNkYmY4NTljODhkN2ZiNiIsImV4cCI6MTYzODQyNzE4MywiaWF0IjoxNjM4NDIzNTgzLCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmNkYjA0OGU1ODQyOTU0MjA4NDAzZWJkMjBkYTFhYThlZiJ9.m1-0nRrOsgL4O4QaKyHL9JUiZW-ZsxsQAF_cFgj5nf8&download=image.png "")

- **未收到响应**

如果我们把half消息发送给MQ了，MQ给保存下来了，但是MQ返回给我们的响应我们没收到呢？此时会发生什么事情？这个时候我们没收到响应，可能就会网络超时报错，也可能直接有其他的异常错误，这个时候订单系统会误以为是发送half消息到MQ失败了。接下来的逻辑则与收到失败响应一致，进行事务回滚。

但这时half消息已经存储在rocketmq中了，消息该如何处理：

RocketMQ这里有一个补偿流程，他会去扫描自己处于half状态的消息，如果我们一直没有对这个消息执行commit/rollback操作，超过了一定的时间，他就会回调你的订单系统的一个接口，去查看这条消息是怎么回，应该怎么处理。

例如会查询订单的状态，查询到订单已关闭，则直接删除该条half消息即可。若查询到改订单已经确认，则直接提交该条half消息。

### 1.2发送commit消息

当本地一些业务事务执行成功后，生产者发送一条commit消息给mq，mq对这条half消息进commit操作，commit后，消费者就能看到该条消息。

![](https://tcs.teambition.net/storage/312c2b4c3f4c6b7764986b1eb9d6593e4ba9?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IjVmYWZhYjk1YTNkYmY4NTljODhkN2ZiNiIsImV4cCI6MTYzODQyODQxNSwiaWF0IjoxNjM4NDI0ODE1LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmMyYjRjM2Y0YzZiNzc2NDk4NmIxZWI5ZDY1OTNlNGJhOSJ9.9IrLyXnU0cD2PsFyHvfM8-ZdF-EtKflUFF7qNDRpNnA&download=image.png "")

### 1.3补偿机制

如果commit或者rollback失败呢？此时还是会触发rocketmq的补偿机制，询问业务系统结果，根据结果对消息进行删除或者commit。

如果订单系统收到了half消息写入成功的响应了，同时尝试对自己的数据库更新了，然后根据失败或者成功去执行了rollback或者commit请求，发送给MQ了。很不巧，mq在这个时候挂掉了，导致rollback或者commit请求发送失败，怎么办？

如果是这种情况的话，那就等mq自己重启了，重启之后他会扫描half消息，然后还是通过上面说到的补偿机制，去回调你的接口。

![](https://tcs.teambition.net/storage/312c3c7e27503e6076b2f7dcd8a7d435a76f?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IjVmYWZhYjk1YTNkYmY4NTljODhkN2ZiNiIsImV4cCI6MTYzODQyODY2OCwiaWF0IjoxNjM4NDI1MDY4LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmMzYzdlMjc1MDNlNjA3NmIyZjdkY2Q4YTdkNDM1YTc2ZiJ9.xg-3ztkAkc5Eyr-D-yGiCvJl7yXHsEaMvtT8CpQvJv8&download=image.png "")

### 1.4事务消息的原理

- **half消息原理及发送**

发送half消息后，后将消息写入broker中的commitLog文件，但是broker发现这是条half消息后，不会将消息的offset写入consumerqueue中，二是写入到内部一个topic为“**RMQ_SYS_TRANS_HALF_TOPIC**”的ConsunerQueue中，因此你的消费服务肯定是取不到相关消息的。

![](https://tcs.teambition.net/storage/312c67083f3ee1374e584d8d9fb9a8ec39bd?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IjVmYWZhYjk1YTNkYmY4NTljODhkN2ZiNiIsImV4cCI6MTYzODQyOTkzOSwiaWF0IjoxNjM4NDI2MzM5LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmM2NzA4M2YzZWUxMzc0ZTU4NGQ4ZDlmYjlhOGVjMzliZCJ9.R_m8dVniP3vDVX7Ej05TyFH25tDHoNobjKwN-nLHbdI&download=image.png "")

- **half消息成功响应**

half消息进入到RocketMQ内部的RMQ_SYS_TRANS_HALF_TOPIC的ConsumeQueue文件中，此时就会认为half消息写入成功了，然后就会返回响应给订单系统。所以这个时候，一旦你的订单系统收到这个half消息写入成功的响应，必然就知道这个half消息已经在RocketMQ内部了。

![](https://tcs.teambition.net/storage/312cf3e7e3a07dc27a2333992d58381dec5d?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IjVmYWZhYjk1YTNkYmY4NTljODhkN2ZiNiIsImV4cCI6MTYzODQzMDE5MywiaWF0IjoxNjM4NDI2NTkzLCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmNmM2U3ZTNhMDdkYzI3YTIzMzM5OTJkNTgzODFkZWM1ZCJ9.ipoAj0YS5_ZRbpfXgjv4p3vP9QZ2ypT19tE_oBBl39s&download=image.png "")

- **补偿机制原理**

在后台有定时任务，定时任务会去扫描RMQ_SYS_TRANS_HALF_TOPIC中的half消息，如果你超过一定时间还是half消息，他会回调订单系统的接口，让你判断这个half消息是要rollback还是commit。

![](https://tcs.teambition.net/storage/312c369537146913ac197172c920eb18bbb1?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IjVmYWZhYjk1YTNkYmY4NTljODhkN2ZiNiIsImV4cCI6MTYzODQzMDI5NSwiaWF0IjoxNjM4NDI2Njk1LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmMzNjk1MzcxNDY5MTNhYzE5NzE3MmM5MjBlYjE4YmJiMSJ9.0COXUf0l80a_lfR6SbTwpLz_TVzIMraT_M6ZO3rbb1Y&download=image.png "")

- **消息回滚消息回滚**

消息回滚并不会去删除commitlog中的消息，因为RocketMQ都是顺序把消息写入磁盘文件的，所以在这里如果你执行rollback，他的本质就是用一个OP操作来标记half消息的状态，标记为rollback。

- **消息commit**

你执行commit操作之后，RocketMQ就会在OP_TOPIC里写入一条记录，标记half消息已经是commit状态了。接着需要把放在RMQ_SYS_TRANS_HALF_TOPIC中的half消息给写入到OrderPaySuccessTopic的ConsumeQueue里去，然后我们的消费系统可以就可以看到这条消息进行消费了。

![](https://tcs.teambition.net/storage/312c1d29dd4db5b00b05dc6afeabf622cfcb?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IjVmYWZhYjk1YTNkYmY4NTljODhkN2ZiNiIsImV4cCI6MTYzODQzMDYwNSwiaWF0IjoxNjM4NDI3MDA1LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmMxZDI5ZGQ0ZGI1YjAwYjA1ZGM2YWZlYWJmNjIyY2ZjYiJ9.yGh1ppkLapnIjOaJdHpXB2IFcZPNCVKNO621dE5EEGc&download=image.png "")

## 2.解决mq自身丢失消息

### 2.1同步刷盘

将刷盘模式改为同步刷盘，改为同步刷盘后，写入MQ的每条消息，只要MQ告诉我们写入成功了，那么他们就是已经进入了磁盘文件了。

### 2.2主从架构避免磁盘故障

必须让一个Master Broker有一个Slave Broker去同步他的数据，而且你一条消息写入成功，必须是让Slave Broker也写入成功，保证数据有多个副本的冗余。你一条消息但凡写入成功了，此时主从两个Broker上都有这条数据了，此时如果你的Master Broker的磁盘坏了，但是lave Broker上至少还是有数据的，数据是不会因为磁盘故障而丢失的。

## 3.解决消费者消息丢失——**手动提交offset + 自动故障转移**

RocketMQ的消费者中会注册一个监听器，就是上面小块代码中的MessageListenerConcurrently这个东西，当你的消费者获取到一批消息之后，就会回调你的这个监听器函数，让你来处理这一批消息。

然后当你处理完毕之后，你才会返ConsumeConcurrentlyStatus.CONSUME_SUCCESS作为消费成功的示意，告诉RocketMQ，这批消息我已经处理完毕了。

如果是红包系统获取到一批消息之后，还没处理完，也就没返回ConsumeConcurrentlyStatus.CONSUME_SUCCESS这个状态呢，自然没提交这批消息的offset给broker呢，此时消费系统突然挂了，会怎么样？

其实在这种情况下，你对一批消息都没提交他的offset给broker的话，broker不会认为你已经处理完了这批消息，此时你突然消费系统的一台机器宕机了，他其实会感知到你的红包系统的一台机器作为一个Consumer挂了。

接着他会把你没处理完的那批消息交给红包系统的其他机器去进行处理，所以在这种情况下，消息也绝对是不会丢失的

![](https://tcs.teambition.net/storage/312c15d3f01b2f83f70f8fda00c2c6b62102?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IjVmYWZhYjk1YTNkYmY4NTljODhkN2ZiNiIsImV4cCI6MTYzODQzMjQ2MSwiaWF0IjoxNjM4NDI4ODYxLCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMmMxNWQzZjAxYjJmODNmNzBmOGZkYTAwYzJjNmI2MjEwMiJ9.taPsOPJN9EEWuEzje_z5qTYSgsNTH9zVO_4GojwZZ4s&download=image.png "")

# 三、总结

## 1.发送消息到MQ的零丢失

- 方案一：同步发送消息 + 反复多次重试

- 方案二：事务消息机制

两者都有保证消息发送零丢失的效果，但事务消息方案整体会更好一些。

## 2.MQ收到消息之后的零丢失

- 开启同步刷盘策略 

- 主从架构同步机制，只要让一个Broker收到消息之后同步写入磁盘，同时同步复制给其他Broker，然后再返回响应给生产者说写入成功，此时就可以保证MQ自己不会弄丢消息

## 3.消费消息的零丢失

**手动提交offset + 自动故障转移：**采用RocketMQ的消费者天然就可以保证你处理完消息之后，才会提交消息的offset到broker去。













































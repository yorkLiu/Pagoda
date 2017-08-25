import tensorflow as tf

one = tf.placeholder(tf.float32)
two = tf.placeholder(tf.float32)

result = tf.multiply(one, two)

with tf.Session() as sess:
    print sess.run(result, feed_dict={one:[6.], two:[5.]})
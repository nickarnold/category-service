package com.levelsbeyond.execserver.actor;

import java.util.concurrent.atomic.AtomicInteger;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;


public class ExecActor extends UntypedActor {
	private static AtomicInteger num = new AtomicInteger(0);

	@Override
	public void preStart() {
		// create the greeter actor
		final ActorRef greeter =
				getContext().actorOf(Props.create(ExecActor.class), "execActor");

		if (num.intValue() <= 15) {
			Message m = new Message();
			m.number = num.incrementAndGet();
			m.message = "doYourThing-" + m.number;

			// tell it to perform the greeting
			greeter.tell(m, getSelf());

		}
	}

	@Override
	public void onReceive(Object msg) {
		if (msg instanceof Message) {
			Message m = (Message) msg;
			if (m.number <= 10) {
				System.out.println(m.message);
			}
			else {
				super.unhandled("Number is too damn high!");
			}
		}
		else {
			unhandled(msg);
		}
	}

	public static class Message {
		String message;
		int number;
	}
}

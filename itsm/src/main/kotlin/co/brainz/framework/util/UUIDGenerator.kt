package co.brainz.framework.util

import java.util.UUID

public class UUIDGenerator {
	val uuid: String
		get() {
			return UUID.randomUUID().toString().replace("-", "")
		}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			for (i in 0..9) {
				println(UUID.randomUUID().toString().replace("-", ""))
			}
		}
	}
}